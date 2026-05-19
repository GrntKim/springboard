import { apiClient } from "./client";

export type Comment = {
    id: number;
    postId: number;
    userId: number;
    authorName: string;
    content: string;
    createdAt: string;
};

export type CreateCommentRequest = {
    postId: number;
    userId: number;
    content: string;
};

export async function getCommentsByPostId(postId: number) {
    const res = await apiClient.get<Comment[]>(`/posts/${postId}/comments`);
    return res.data;
}

export async function createComment(request: CreateCommentRequest) {
    await apiClient.post("/comments", request);
}