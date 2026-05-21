import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
    getCurrentUser,
    login, 
    logout,
    type LoginRequest,
} from "../api/auth"

const AUTH_ME_QUERY_KEY = ["auth", "me"];

export function useAuth() {
    const queryClient = useQueryClient();

    const meQuery = useQuery({
        queryKey: AUTH_ME_QUERY_KEY,
        queryFn: getCurrentUser,
        retry: false,
    });

    const loginMutation = useMutation({
        mutationFn: (request: LoginRequest) => login(request),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: AUTH_ME_QUERY_KEY });
        },
    });

    const logoutMutation = useMutation({
        mutationFn: logout,
        onSuccess: () => {
            queryClient.setQueryData(AUTH_ME_QUERY_KEY, null);
        }
    });

    return {
        currentUser: meQuery.data ?? null,
        isAuthChecked: !meQuery.isPending,
        isLoggedIn: !!meQuery.data,
        login: loginMutation.mutateAsync,
        logout: logoutMutation.mutateAsync,
        isLoginPending: loginMutation.isPending,
        isLogoutPending: logoutMutation.isPending,
    };
}