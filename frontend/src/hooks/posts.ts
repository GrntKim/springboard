import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
    createPost,
    deletePost,
    getPostById,
    getPosts,
    getPostsByUserId,
    updatePost,
    type CreatePostRequest,
    type UpdatePostRequest,
} from "../api/posts"
import { USER_QUERY_KEY } from "./users";

export const POSTS_QUERY_KEY = {
    all: ["posts"] as const,
    detail: (postId: number) => ["posts", postId] as const,
    comments: (postId: number) => ["posts", postId, "comments"] as const,
};

export function usePosts() {
    return useQuery({
        queryKey: POSTS_QUERY_KEY.all,
        queryFn: getPosts,
    });
}

export function usePost(postId: number) {
    return useQuery({
        queryKey: POSTS_QUERY_KEY.detail(postId),
        queryFn: () => getPostById(postId),
        enabled: Number.isFinite(postId),
    });
}

export function usePostsByUserId(userId: number) {
    return useQuery({
        queryKey: USER_QUERY_KEY.posts(userId),
        queryFn: () => getPostsByUserId(userId),
        enabled: Number.isFinite(userId),
    });
}

export function useCreatePost() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (request: CreatePostRequest) => createPost(request),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: POSTS_QUERY_KEY.all,
            });
        },
    });
}

export function useUpdatePost(postId: number, authorId?: number) {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (request: UpdatePostRequest) => updatePost(postId, request),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: POSTS_QUERY_KEY.detail(postId),
            });
            queryClient.invalidateQueries({
                queryKey: POSTS_QUERY_KEY.all
            });
            if (authorId !== undefined) {
                queryClient.invalidateQueries({
                    queryKey: USER_QUERY_KEY.posts(authorId),
                });
            }
        },
    });
}

export function useDeletePost(postId: number, authorId?: number) {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: () => deletePost(postId),
        onSuccess: () => {
            queryClient.removeQueries({
                queryKey: POSTS_QUERY_KEY.detail(postId),
            });

            queryClient.removeQueries({
                queryKey: POSTS_QUERY_KEY.comments(postId),
            });

            queryClient.invalidateQueries({
                queryKey: POSTS_QUERY_KEY.all,
            });

            if (authorId !== undefined) {
                queryClient.invalidateQueries({
                    queryKey: USER_QUERY_KEY.posts(authorId),
                });
            }
        },
    });
}