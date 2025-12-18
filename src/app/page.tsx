import Link from 'next/link'
import Image from 'next/image'

// Mock data until DB is connected
const ARTWORKS = [
    {
        id: 1,
        title: 'Sunset Dreams',
        image: 'https://images.unsplash.com/photo-1517404215738-15263e9f9178?q=80&w=800&auto=format&fit=crop',
        artist: 'Jane Doe',
        artistImg: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Jane',
        category: 'Painting',
    },
    {
        id: 2,
        title: 'Neon City',
        image: 'https://images.unsplash.com/photo-1469334031218-e382a71b716b?q=80&w=800&auto=format&fit=crop',
        artist: 'CyberArtist',
        artistImg: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Cyber',
        category: 'Digital',
    },
    {
        id: 3,
        title: 'Abstract Thoughts',
        image: 'https://images.unsplash.com/photo-1541963463532-d68292c34b19?q=80&w=800&auto=format&fit=crop',
        artist: 'Picasso Jr.',
        artistImg: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Pablo',
        category: 'Abstract',
    },
]

export default function Home() {
    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div className="text-center mb-12">
                <h1 className="text-4xl font-extrabold tracking-tight text-gray-900 sm:text-5xl md:text-6xl">
                    Discover & Rate <span className="text-blue-600">Great Art</span>
                </h1>
                <p className="mt-4 max-w-2xl mx-auto text-xl text-gray-500">
                    Join the community of artists and art lovers. Share your work, get feedback, and find inspiration.
                </p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
                {ARTWORKS.map((art) => (
                    <Link key={art.id} href={`/artwork/${art.id}`} className="group">
                        <div className="bg-white rounded-xl shadow-sm overflow-hidden hover:shadow-md transition-shadow duration-300">
                            <div className="relative aspect-[4/3] w-full overflow-hidden bg-gray-200">
                                <Image
                                    src={art.image}
                                    alt={art.title}
                                    fill
                                    className="object-cover group-hover:scale-105 transition-transform duration-300"
                                    sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
                                />
                            </div>
                            <div className="p-4">
                                <div className="flex justify-between items-start">
                                    <div>
                                        <h3 className="text-lg font-semibold text-gray-900 group-hover:text-blue-600 transition-colors">
                                            {art.title}
                                        </h3>
                                        <p className="text-sm text-gray-500">{art.category}</p>
                                    </div>
                                </div>
                                <div className="mt-4 flex items-center">
                                    <div className="flex-shrink-0">
                                        <img
                                            className="h-8 w-8 rounded-full bg-gray-100" // using img for quick avatar mock
                                            src={art.artistImg}
                                            alt={art.artist}
                                        />
                                    </div>
                                    <div className="ml-3">
                                        <p className="text-sm font-medium text-gray-900">{art.artist}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Link>
                ))}
            </div>
        </div>
    )
}
