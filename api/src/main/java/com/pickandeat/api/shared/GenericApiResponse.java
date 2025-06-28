package com.pickandeat.api.shared;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "GenericApiResponse", description = "Standard API response wrapper")
public class GenericApiResponse<T> {
    @Schema(description = "Descriptive success or error message")
    private final String message;

    @Schema(description = "Payload data (may be null)")
    private final T data;

    public GenericApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }
}
