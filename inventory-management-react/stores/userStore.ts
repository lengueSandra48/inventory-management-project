import { create } from "zustand"

export type User = {
    id: number
    email: string
    firstName: string
    lastName: string
    role: string
    roles?: Array<{
        id: number
        roleName: string
        utilisateurId: number
        entrepriseId: number
    }>
} | null

export type UserStore = {
    user: User
    setUser: (user: User) => void
    clearUser: () => void
}

export const useUserStore = create<UserStore>((set) => ({
    user: null,
    setUser: (user) => set({ user }),
    clearUser: () => set({ user: null }),
}))