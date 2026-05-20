import axios from "axios";

export const HTTP_STATUS = {
    BAD_REQUEST: 400,
    NOT_FOUND: 404,
    CONFLICT: 409,
    INTERNAL_SERVER_ERROR: 500,
} as const;

export const API_ERROR_MESSAGE = {
    BAD_REQUEST: "Please check your input.",
    SERVER_ERROR: "Something went wrong.",
} as const;

export function getApiStatus(error: unknown): number | undefined {
    if (!axios.isAxiosError(error)) {
        return undefined;
    }

    return error.response?.status;
}

export function getApiErrorMessage(
    error: unknown,
    messagesByStatus: Record<number, string> = {}
): string {
    const status = getApiStatus(error);

    if (status && messagesByStatus[status]) {
        return messagesByStatus[status];
    }

    return API_ERROR_MESSAGE.SERVER_ERROR;
}