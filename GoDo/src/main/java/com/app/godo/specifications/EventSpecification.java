package com.app.godo.specifications;

import com.app.godo.enums.EventType;
import com.app.godo.models.Event;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class EventSpecification {
    public static <T> Specification<T> empty() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Event> hasText(String filter) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(filter)) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + filter.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("venue").get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), likePattern)
            );
        };
    }

    public static Specification<Event> priceIsGreaterThanOrEqual(double priceFrom) {
        return (root, query, criteriaBuilder) ->
                priceFrom < 0 ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceFrom);
    }

    public static Specification<Event> priceIsLessThanOrEqual(double priceTo) {
        return (root, query, criteriaBuilder) ->
                priceTo < 0 ? criteriaBuilder.conjunction() : criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo);
    }

    public static Specification<Event> hasEventType(EventType eventType) {
        return (root, query, criteriaBuilder) ->
                eventType == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("eventType"), eventType);
    }

    public static Specification<Event> dateIsEqualTo(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                date == null  ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("date"), date);
    }
}
