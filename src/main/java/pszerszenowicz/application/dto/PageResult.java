package pszerszenowicz.application.dto;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        int page,
        int totalPages,
        long totalElements
) {
}