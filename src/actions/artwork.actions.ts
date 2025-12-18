'use server'

import { ArtworkService } from '@/services/artwork.service';
import { redirect } from 'next/navigation';
import { revalidatePath } from 'next/cache';

/**
 * Server Actions act as the "Controller" in Next.js App Router.
 * They handle the request, parse inputs, call the service layer, and manage the response (redirect/revalidate).
 */

export async function uploadArtwork(prevState: any, formData: FormData) {
    try {
        const title = formData.get('title') as string;
        const category = formData.get('category') as string;
        const description = formData.get('description') as string;
        const imageFile = formData.get('file-upload') as File;

        if (!imageFile || imageFile.size === 0) {
            return { message: 'Please select an image to upload.' };
        }

        await ArtworkService.create(title, category, description, imageFile);

        revalidatePath('/'); // Refresh the gallery
    } catch (error: any) {
        console.error('Upload failed:', error);
        return { message: error.message || 'Failed to upload artwork.' };
    }

    redirect('/'); // Navigate to home on success
}
