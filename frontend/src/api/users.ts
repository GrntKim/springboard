import { apiClient } from "./client";

export type User = {
    id: number;
    username: string;
    displayName: string;
    createdAt: string;
};

export type CreateUserRequest = {
    username: string;
    password: string;
    displayName: string;
};

export async function getUsers() {
    const res = await apiClient.get<User[]>("/users");
    return res.data;
}

export async function getUser(userId: number) {
    const res = await apiClient.get<User>(`/users/${userId}`);
    return res.data;
}

export async function createUser(request: CreateUserRequest) {
    await apiClient.post("/users", request);
}