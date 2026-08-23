import React from 'react'
import { BrowserRouter } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import { CartProvider } from './context/CartContext'
import { ProductProvider } from './context/ProductContext'
import AppRoutes from './routes/AppRoutes'
import Header from './components/common/Header'
import Footer from './components/common/Footer'
import { Toaster } from 'react-hot-toast'

const App: React.FC = () => {
	return (
		<BrowserRouter>
			<AuthProvider>
				<CartProvider>
					<ProductProvider>
						<div className="flex flex-col min-h-screen">
							<Header />
							<main className="flex-grow">
								<AppRoutes />
							</main>
							<Footer />
							<Toaster position="top-right" />
						</div>
					</ProductProvider>
				</CartProvider>
			</AuthProvider>
		</BrowserRouter>
	)
}

export default App
