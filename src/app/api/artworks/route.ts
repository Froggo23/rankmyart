import { NextResponse } from 'next/server';
import { ArtworkService } from '@/services/artwork.service';

/**
 * Route Handler for /api/artworks
 * Acts as a REST Controller (e.g., @RestController @RequestMapping("/api/artworks"))
 */
export async function GET() {
    try {
        const artworks = await ArtworkService.findAll();
        return NextResponse.json(artworks);
    } catch (error: any) {
        return NextResponse.json({ error: error.message }, { status: 500 });
    }
}

export async function POST(request: Request) {
    try {
        const formData = await request.formData();
        const title = formData.get('title') as string;
        const category = formData.get('category') as string;
        const description = formData.get('description') as string;
        const imageFile = formData.get('file-upload') as File;

        if (!imageFile) {
            return NextResponse.json({ error: 'Image required' }, { status: 400 });
        }

        const newArtwork = await ArtworkService.create(title, category, description, imageFile);
        return NextResponse.json(newArtwork, { status: 201 });

    } catch (error: any) {
        return NextResponse.json({ error: error.message }, { status: 500 });
    }
}
