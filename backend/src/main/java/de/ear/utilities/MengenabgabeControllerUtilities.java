package de.ear.utilities;

import de.ear.model.Mengenabgabe;
import de.ear.model.MengenabgabePage;
import de.ear.services.GeraeteartenService;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MengenabgabeControllerUtilities {

    private static final Logger LOGGER = LoggerFactory.getLogger(MengenabgabeControllerUtilities.class);
    public static final int ZERO = 0;
    public static final int TWENTY = 20;

    public static Sort createSort(String[] sortParameters) {
        Set<String> allowedProperties = Set.of(
                "id",
                "menge",
                "einheit",
                "jahr",
                "monat",
                "geraeteartnummer",
                "registrierungsnummer"
        );
        if (sortParameters == null || sortParameters.length == 0) {
            return Sort.unsorted();
        }
        List<String> sortTokens = new ArrayList<>();
        for (String parameter : sortParameters) {
            for (String token : parameter.split(",", -1)) {
                sortTokens.add(token.trim());
            }
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (int index = 0; index < sortTokens.size(); index++) {
            String property = sortTokens.get(index);
            if (!allowedProperties.contains(property)) {
                LOGGER.error("Ungültiges Sortierfeld: {}", property);
                throw new IllegalArgumentException(
                        "Ungültiges Sortierfeld: " + property);
            }
            Sort.Direction direction = Sort.Direction.ASC;
            if (index + 1 < sortTokens.size()) {
                String next = sortTokens.get(index + 1);
                if (next.equalsIgnoreCase("asc") || next.equalsIgnoreCase("desc")) {
                    direction = Sort.Direction.fromString(next);
                    index++;
                }
            }
            orders.add(new Sort.Order(direction, property));
        }
        return Sort.by(orders);
    }

    public static @NonNull Pageable getPageable(Integer page, Integer size, String[] sort) {
        int pageNumber = page != null ? page : ZERO;
        int pageSize = size != null ? size : TWENTY;
        Sort sorting = createSort(sort);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sorting);
        return pageable;
    }

    public static @NonNull MengenabgabePage createResponse(Page<Mengenabgabe> result) {
        MengenabgabePage response = new MengenabgabePage();
        response.setContent(result.getContent().toArray(Mengenabgabe[]::new));
        response.setPage(result.getNumber());
        response.setSize(result.getSize());
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());
        return response;
    }
}
