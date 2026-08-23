import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getCategories } from '../../api/categoryApi';
import { Category } from '../../types/Category';

const CategoryList: React.FC = () => {
  const [categories, setCategories] = useState<Category[]>([]);

  useEffect(() => {
    getCategories().then(res => setCategories(res.data)).catch(console.error);
  }, []);

  return (
    <div className="flex flex-wrap gap-4">
      {categories.map((cat) => (
        <Link
          key={cat.id}
          to={`/categories/${cat.id}`}
          className="bg-white shadow rounded-lg px-4 py-2 hover:shadow-md transition"
        >
          {cat.name}
        </Link>
      ))}
    </div>
  );
};

export default CategoryList;