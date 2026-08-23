import React, { useState } from 'react';

interface ProductFilterProps {
  onSearch: (query: string) => void;
  onCategoryChange: (categoryId: string) => void;
  categories: { id: string; name: string }[];
}

const ProductFilter: React.FC<ProductFilterProps> = ({ onSearch, onCategoryChange, categories }) => {
  const [query, setQuery] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(query);
  };

  return (
    <div className="flex flex-col md:flex-row gap-4 mb-6">
      <form onSubmit={handleSubmit} className="flex-1 flex">
        <input
          type="text"
          placeholder="Search products..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="flex-1 border rounded-l px-4 py-2 focus:outline-none focus:ring-2 focus:ring-green-500"
        />
        <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded-r hover:bg-green-700">
          Search
        </button>
      </form>
      <select
        onChange={(e) => onCategoryChange(e.target.value)}
        className="border rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-green-500"
      >
        <option value="">All Categories</option>
        {categories.map((cat) => (
          <option key={cat.id} value={cat.id}>{cat.name}</option>
        ))}
      </select>
    </div>
  );
};

export default ProductFilter;