import { apiClient } from "./client";

export type User = {
    id: number;
    loginId: string;
    nickname: string;
    createdAt: string;
};

export type CreateUserRequest = {
    loginId: string;
    password: string;
    nickname: string;
};

export type UpdateUserRequest = {
    loginId: string;
    password: string;
    nickname: string;
}

export async function getUsers() {
    const res = await apiClient.get<User[]>("/users");
    return res.data;
}

export async function getUserById(userId: number) {
    const res = await apiClient.get<User>(`/users/${userId}`);
    return res.data;
}

export async function createUser(request: CreateUserRequest): Promise<void> {
    await apiClient.post("/users", request);
}

export async function updateUser(userId: number, request: UpdateUserRequest): Promise<void> {
    await apiClient.put(`/users/${userId}`, request)
}

export async function deleteUser(userId: number): Promise<void> {
    await apiClient.delete(`/users/${userId}`);
}