import Link from 'next/link'

export default function Navbar() {
    return (
        <nav className="bg-white border-b border-gray-200 sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex">
                        <Link href="/" className="flex-shrink-0 flex items-center">
                            <span className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-purple-600 to-blue-500">
                                RankMyArt
                            </span>
                        </Link>
                        <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
                            <Link
                                href="/"
                                className="inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium text-gray-500 hover:text-gray-900 hover:border-gray-300 transition-colors"
                            // active class logic would go here
                            >
                                Gallery
                            </Link>
                            <Link
                                href="/trending"
                                className="inline-flex items-center px-1 pt-1 border-b-2 border-transparent text-sm font-medium text-gray-500 hover:text-gray-900 hover:border-gray-300 transition-colors"
                            >
                                Trending
                            </Link>
                        </div>
                    </div>
                    <div className="flex items-center space-x-4">
                        <Link
                            href="/upload"
                            className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
                        >
                            Upload Art
                        </Link>
                        <Link
                            href="/login"
                            className="text-sm font-medium text-gray-500 hover:text-gray-900"
                        >
                            Log in
                        </Link>
                    </div>
                </div>
            </div>
        </nav>
    )
}
