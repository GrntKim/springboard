import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { 
    getUsers, 
    getUserById, 
    type CreateUserRequest, 
    createUser, 
    type UpdateUserRequest, 
    updateUser, 
    deleteUser
} from "../api/users";

export const USER_QUERY_KEY = {
    all: ["users"] as const,
    detail: (userId: number) => ["users", userId] as const,
    posts: (userId: number) => ["users", userId, "posts"] as const,
    comments: (userId: number) => ["users", userId, "comments"] as const,
};

export function useUsers() {
    return useQuery({
        queryKey: USER_QUERY_KEY.all,
        queryFn: getUsers,
    });
}

export function useUser(userId: number) {
    return useQuery({
        queryKey: USER_QUERY_KEY.detail(userId),
        queryFn: () => getUserById(userId),
        enabled: Number.isFinite(userId),
    });
}

export function useCreateUser() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (request: CreateUserRequest) => createUser(request),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: USER_QUERY_KEY.all,
            });
        },
    });
}

export function useUpdateUser(userId: number) {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: (request: UpdateUserRequest) => updateUser(userId, request),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: USER_QUERY_KEY.all,
            });
            queryClient.invalidateQueries({
                queryKey: USER_QUERY_KEY.detail(userId),
            });
        },
    });
}

export function useDeleteUser(userId: number) {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: () => deleteUser(userId),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: USER_QUERY_KEY.all,
            });
            queryClient.removeQueries({
                queryKey: USER_QUERY_KEY.detail(userId),
            });
            queryClient.removeQueries({
                queryKey: USER_QUERY_KEY.posts(userId),
            });
            queryClient.removeQueries({
                queryKey: USER_QUERY_KEY.comments(userId),
            });
        },
    });
}