import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { 
    createComment, 
    deleteComment, 
    getCommentById, 
    getComments, 
    getCommentsByPostId, 
    updateComment, 
    type CreateCommentRequest, 
    type UpdateCommentRequest 
} from "../api/comments";
import { POSTS_QUERY_KEY } from "./posts";
import { USER_QUERY_KEY } from "./users";

export const COMMENTS_QUERY_KEY = {
    all: ["comments"] as const,
    detail: (commentId: number) => ["comments", commentId] as const,
};

export function useComments() {
    return useQuery({
        queryKey: COMMENTS_QUERY_KEY.all,
        queryFn: getComments,
    });
}

export function useCommentById(commentId: number) {
    return useQuery({
        queryKey: COMMENTS_QUERY_KEY.detail(commentId),
        queryFn: () => getCommentById(commentId),
        enabled: Number.isFinite(commentId),
    });
}

export function useCommentsByPostId(postId: number) {
    return useQuery({
        queryKey: POSTS_QUERY_KEY.comments(postId),
        queryFn: () => getCommentsByPostId(postId),
        enabled: Number.isFinite(postId),
    });
}

export function useCreateComment() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (request: CreateCommentRequest) => createComment(request),
        onSuccess: (_, request) => {
            queryClient.invalidateQueries({
                queryKey: COMMENTS_QUERY_KEY.all,
            });

            queryClient.invalidateQueries({
                queryKey: POSTS_QUERY_KEY.comments(request.postId),
            })
        },
    });
}

export function useUpdateComment(commentId: number, postId?: number, authorId?: number) {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (request: UpdateCommentRequest) => updateComment(commentId, request),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: COMMENTS_QUERY_KEY.detail(commentId),
            });
            queryClient.invalidateQueries({
                queryKey: COMMENTS_QUERY_KEY.all,
            });

            if (postId !== undefined) {
                queryClient.invalidateQueries({
                    queryKey: POSTS_QUERY_KEY.comments(postId),
                })
            }

            if (authorId !== undefined) {
                queryClient.invalidateQueries({
                    queryKey: USER_QUERY_KEY.comments(authorId),
                })
            }
        },
    });
}

export function useDeleteComment(commentId: number, postId?: number, authorId?: number) {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: () => deleteComment(commentId),
        onSuccess: () => {
            queryClient.removeQueries({
                queryKey: COMMENTS_QUERY_KEY.detail(commentId),
            });

            queryClient.invalidateQueries({
                queryKey: COMMENTS_QUERY_KEY.all,
            });

            if (authorId !== undefined) {
                queryClient.invalidateQueries({
                    queryKey: USER_QUERY_KEY.comments(authorId),
                });
            }

            if (postId !== undefined) {
                queryClient.invalidateQueries({
                    queryKey: POSTS_QUERY_KEY.comments(postId),
                });
            }

        }
    })
}