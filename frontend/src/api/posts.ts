import { apiClient } from "./client";

export type Post = {
    id: number;
    title: string;
    content: string;
    authorId: number;
    authorName: string;
    createdAt: string;
};

export type CreatePostRequest = {
    userId: number;
    title: string;
    content: string;
};

export type UpdatePostRequest = {
    title: string;
    content: string;
}

export async function getPosts() {
    const res = await apiClient.get<Post[]>("/posts");
    return res.data;
}

export async function getPostById(postId: number) {
    const res = await apiClient.get<Post>(`/posts/${postId}`);
    return res.data;
}

export async function getPostsByUserId(userId: number) {
    const res = await apiClient.get<Post[]>(`/users/${userId}/posts`);
    return res.data;
}

export async function createPost(request: CreatePostRequest): Promise<void> {
    await apiClient.post("/posts", request);
}

export async function updatePost(postId: number, request: UpdatePostRequest): Promise<void> {
    await apiClient.put(`/posts/${postId}`, request);
}

export async function deletePost(postId: number): Promise<void> {
    await apiClient.delete(`/posts/${postId}`);
}