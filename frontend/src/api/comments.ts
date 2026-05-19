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

export type UpdateCommentRequest = {
    content: string;
}

export async function getComments() {
    const res = await apiClient.get<Comment[]>("/comments");
    return res.data;
}

export async function getCommentsByPostId(postId: number) {
    const res = await apiClient.get<Comment[]>(`/posts/${postId}/comments`);
    return res.data;
}

export async function getCommentById(commentId: number) {
    const res = await apiClient.get<Comment>(`/comments/${commentId}`);
    return res.data;
}

export async function createComment(request: CreateCommentRequest): Promise<void> {
    await apiClient.post("/comments", request);
}

export async function updateComment(commentId: number, request: UpdateCommentRequest): Promise<void>{
    await apiClient.put(`/comments/${commentId}`, request);
}

export async function deleteComment(commentId: number): Promise<void>{
    await apiClient.delete(`/comments/${commentId}`);
}