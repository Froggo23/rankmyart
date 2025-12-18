import { createClient } from '@/utils/supabase/server';
import { Artwork } from '@/types/domain';

/**
 * Service class for handling Artwork business logic.
 * Similar to a Spring @Service or @Repository.
 */
export class ArtworkService {

    /**
     * Fetches all artworks.
     * Equivalent to findAll()
     */
    static async findAll(): Promise<Artwork[]> {
        const supabase = await createClient();
        const { data, error } = await supabase
            .from('artworks')
            .select('*')
            .order('created_at', { ascending: false });

        if (error) {
            console.error('Error fetching artworks:', error);
            throw new Error('Failed to fetch artworks');
        }

        return data.map(this.mapToDomain);
    }

    /**
     * Fetches an artwork by ID.
     * Equivalent to findById(Long id)
     */
    static async findById(id: number): Promise<Artwork | null> {
        const supabase = await createClient();
        const { data, error } = await supabase
            .from('artworks')
            .select('*')
            .eq('id', id)
            .single();

        if (error) {
            if (error.code === 'PGRST116') return null; // Not found
            throw new Error('Failed to fetch artwork');
        }

        return this.mapToDomain(data);
    }

    /**
     * Creates a new artwork entry.
     * Uses Supabase Storage for the image and Database for the record.
     * Equivalent to save(Artwork entity)
     */
    static async create(title: string, category: string, description: string, imageFile: File): Promise<Artwork | null> {
        const supabase = await createClient();

        // 1. Upload Image
        const fileExt = imageFile.name.split('.').pop();
        const fileName = `${Date.now()}-${Math.random()}.${fileExt}`;
        const filePath = `artworks/${fileName}`;

        const { error: uploadError } = await supabase.storage
            .from('images')
            .upload(filePath, imageFile);

        if (uploadError) {
            throw new Error('Failed to upload image: ' + uploadError.message);
        }

        // 2. Get Public URL
        const { data: { publicUrl } } = supabase.storage
            .from('images')
            .getPublicUrl(filePath);

        // 3. Get Current User
        const { data: { user } } = await supabase.auth.getUser();
        if (!user) throw new Error('Unauthorized');

        // 4. Insert Record
        const { data, error: insertError } = await supabase
            .from('artworks')
            .insert({
                title,
                category,
                description,
                image_url: publicUrl,
                uploader_id: user.id
            })
            .select()
            .single();

        if (insertError) {
            throw new Error('Failed to save artwork metadata: ' + insertError.message);
        }

        return this.mapToDomain(data);
    }

    // Mapper to convert DB snake_case to Domain camelCase
    private static mapToDomain(row: any): Artwork {
        return {
            id: row.id,
            title: row.title,
            category: row.category,
            description: row.description,
            imageUrl: row.image_url,
            upvotes: row.upvotes,
            views: row.views,
            uploaderId: row.uploader_id,
            createdAt: new Date(row.created_at)
        };
    }
}
