package com.ufone.api.errors;

import com.ufone.api.errors.BaseErrorResponse;

import javax.ws.rs.core.Response;

public class InvalidClaims extends BaseErrorResponse {
        private final String error = "invalid_request";
        private final String errorDescription = "claims value is invalid";

        @Override
        public String getErrorTitle() {
                return this.error;
        }

        @Override
        public String getErrorDescription() {
                return this.errorDescription;
        }
}
