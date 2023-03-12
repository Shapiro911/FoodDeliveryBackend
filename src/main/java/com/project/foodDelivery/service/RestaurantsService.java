package com.project.foodDelivery.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.project.foodDelivery.model.DirectionResult;
import com.project.foodDelivery.model.Product;
import com.project.foodDelivery.model.Restaurant;
import com.project.foodDelivery.model.RestaurantSearch;
import com.project.foodDelivery.repository.RestaurantsRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RestaurantsService {
    @Autowired
    private RestaurantsRepository restaurantsRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<Restaurant> getRestaurants(@NotNull List<Double> coordinates, @NotNull RestaurantSearch restaurantSearch, Integer page, Integer pageSize) throws IOException {
        @Getter
        @AllArgsConstructor
        class SpellSuggestion {
            private List<String> suggestions;
            private Integer positionFrom;
            private Integer positionTo;
        }

        List<Restaurant> restaurantList = new ArrayList<>();
        String address = coordinates.get(0).toString() + " " + coordinates.get(1);
        Point coord = new Point(coordinates.get(1), coordinates.get(0));
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        String searchText = restaurantSearch.getSearchText();

        if (!searchText.isEmpty()) {
//            List<SpellSuggestion> spellSuggestions = new ArrayList<>();
//            JLanguageTool languageTool = new JLanguageTool(new BritishEnglish());
//            List<RuleMatch> matches = languageTool.check(searchText);
//
//            for (RuleMatch match: matches) {
//                List<String> bestSuggestions = new ArrayList<>();
//                for (int i = 0; i < 2 && i < match.getSuggestedReplacements().size(); i++) {
//                    bestSuggestions.add(match.getSuggestedReplacements().get(i));
//                }
//
//                while (bestSuggestions.size() != 2) {
//                    bestSuggestions.add(bestSuggestions.get(bestSuggestions.size() - 1));
//                }
//
//                SpellSuggestion spellSuggestion = new SpellSuggestion(bestSuggestions, match.getFromPos(), match.getToPos());
//                spellSuggestions.add(spellSuggestion);
//            }
//
//            List<String> spellCorrected = new ArrayList<>();
//            spellCorrected.add(searchText);
//
//            if (!spellSuggestions.isEmpty()) {
//                for (int i = 0; i < spellSuggestions.get(0).getSuggestions().size(); i++) {
//                    List<String> textParts = new ArrayList<>();
//                    Integer lastPos = 0;
//
//                    for (SpellSuggestion spellSuggestion : spellSuggestions) {
//                        textParts.add(searchText.substring(lastPos, spellSuggestion.getPositionFrom()));
//                        lastPos = spellSuggestion.getPositionTo();
//                        String typoWord = spellSuggestion.getSuggestions().get(i);
//                        textParts.add(typoWord);
//                    }
//
//                    textParts.add(searchText.substring(lastPos));
//                    spellCorrected.add(textParts.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ""));
//                }
//            }

            List<String> restaurantsByText = new ArrayList<>();
            List<String> productsByText = new ArrayList<>();


            List<String> spellCorrected = new ArrayList<>();
            spellCorrected.add(searchText);
            Query query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matchingAny(spellCorrected.toString().replaceAll("\\[|\\]", ""))).sortByScore();
            for (Restaurant restaurant : mongoTemplate.find(addSortValuesToQuery(query, restaurantSearch, address), Restaurant.class)) {
                restaurantsByText.add(restaurant.getName());
            }

            Query productQuery = TextQuery.queryText(TextCriteria.forDefaultLanguage().matchingAny(spellCorrected.toString().replaceAll("\\[|\\]", ""))).sortByScore();
            for (Product product : mongoTemplate.find(productQuery, Product.class)) {
                productsByText.add(product.getId());
            }

            Query geoQuery = new Query().addCriteria(Criteria.where("address.coord").nearSphere(coord).maxDistance(8000)).addCriteria(new Criteria().orOperator(Criteria.where("name").in(restaurantsByText), Criteria.where("dishes").in(productsByText))).with(pageable);
            List<Restaurant> restaurantListTemp = findRestaurants(geoQuery, restaurantSearch, address);
            restaurantList.addAll(restaurantListTemp);
        } else {
            Query query = new Query().addCriteria(Criteria.where("address.coord").nearSphere(coord).maxDistance(8000)).with(pageable);
            List<Restaurant> restaurantListTemp = findRestaurants(query, restaurantSearch, address);
            restaurantList.addAll(restaurantListTemp);
        }

        return restaurantList;
    }

    public DirectionResult findDirection(String origin, String destination, String mode) {
        String url = String.format("https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=AIzaSyCxyo5J8uJ1O5hzhV4fx2-hGiySpsmMQk4&mode=%s", origin, destination, mode);
        RestTemplate restTemplate = new RestTemplate();

        JsonObject direction = new Gson().fromJson(restTemplate.getForObject(url, String.class), JsonObject.class);

        if(Objects.equals(direction.get("status").toString(), "\"OK\"")) {
            JsonObject legs = direction.get("routes").getAsJsonArray().get(0).getAsJsonObject().get("legs").getAsJsonArray().get(0).getAsJsonObject();
            Long distance = legs.get("distance").getAsJsonObject().get("value").getAsLong();
            Long duration = legs.get("duration").getAsJsonObject().get("value").getAsLong();
            DirectionResult directionResult = new DirectionResult(duration, distance);
            return directionResult;
        } else {
              DirectionResult directionResult = new DirectionResult(0L, 0L);
              return directionResult;
        }
    }

    public List<String> getNearbyBoroughs(String address) {
        List<String> boroughsNY = Arrays.asList("Queens", "Bronx", "Manhattan", "Brooklyn", "Staten Island");
        List<String> nearbyBoroughs = new ArrayList<>();

        for(String borough : boroughsNY) {
            Long distance = findDirection(borough, address, "walking").getDistance();
            if (distance <= 10000 && distance > 0) {
                nearbyBoroughs.add(borough);
            }
        }
        return nearbyBoroughs;
    }

    public Query addSortValuesToQuery(@NotNull Query query, @NotNull RestaurantSearch restaurantSearch, String address) throws IOException {
        List<String> nearbyBoroughs = getNearbyBoroughs(address);
        query.addCriteria(Criteria.where("borough").in(nearbyBoroughs));
        if (!restaurantSearch.getSortValues().getPriceRange().isEmpty()) {
            query.addCriteria(Criteria.where("price").in(restaurantSearch.getSortValues().getPriceRange()));
        }
        if (restaurantSearch.getSortValues().getFee() > 0) {
            query.addCriteria(Criteria.where("fee").lte(restaurantSearch.getSortValues().getFee()));
        }

        if (Objects.equals(restaurantSearch.getSortValues().getSortBy(), "popular")) {
            query.addCriteria(Criteria.where("rating").gte(4));
        } else if (Objects.equals(restaurantSearch.getSortValues().getSortBy(), "rating")) {
            query.with(Sort.by(Sort.Direction.DESC, "rating"));
        }
        return query;
    }

    public List<Restaurant> findRestaurants(Query query, RestaurantSearch restaurantSearch, String address) throws IOException {
        List<Restaurant> restaurantList = new ArrayList<>();

        for (Restaurant restaurant : mongoTemplate.find(addSortValuesToQuery(query, restaurantSearch, address), Restaurant.class)) {
            System.out.println(restaurantList + "rList");
            String coordinatesRestaurant = restaurant.getAddress().getCoord().get(1).toString() + " " + restaurant.getAddress().getCoord().get(0).toString();
            Long duration = findDirection(address, coordinatesRestaurant, "bicycling").getDuration();

            if (duration != 0) {
                int durationRounded = Math.round((duration / 60 + 5) / 10) * 10;
                if (durationRounded < 10) {
                    restaurant.setDuration("10-25");
                } else if (durationRounded > (duration / 60)) {
                    restaurant.setDuration(durationRounded + "-" + (durationRounded + 15));
                } else {
                    restaurant.setDuration(durationRounded + 5 + "-" + (durationRounded + 15));
                }

            restaurantList.add(restaurant);
            }
        }
        return restaurantList;
    }
}

