export interface Artwork {
    id: number;
    title: string;
    category: string;
    description: string;
    imageUrl: string;
    upvotes: number;
    views: number;
    uploaderId: string;
    createdAt: Date;
}

export interface ArtworkDTO {
    title: string;
    category: string;
    description: string;
    imageFile: File; // For upload
}
