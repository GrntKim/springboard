import { apiClient } from "./client";

export type RegisterRequest = {
    loginId: string;
    password: string;
    nickname: string;
};

export type LoginRequest = {
    loginId: string;
    password: string;
};

export type CurrentUserResponse = {
    id: number;
    loginId: string;
    nickname: string;
};

export async function register(request: RegisterRequest): Promise<void> {
    return await apiClient.post("/auth/register", request);
}

export async function login(request: LoginRequest): Promise<void> {
    return await apiClient.post("/auth/login", request);
}

export async function logout(): Promise<void> {
    return await apiClient.post("/auth/logout");
}

export async function getCurrentUser(): Promise<CurrentUserResponse> {
    return (await apiClient.get<CurrentUserResponse>("/auth/me")).data;
}