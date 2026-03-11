import { useState, useMemo } from 'react';
import BookListComponent from './BookListComponent';

const PAGE_SIZE = 4;

export default function BookGalleryComponent({ books, loading }) {
  const [page, setPage] = useState(0);

  const totalPages = Math.max(1, Math.ceil(books.length / PAGE_SIZE));

  const safePageIndex = Math.min(page, totalPages - 1);
  if (safePageIndex !== page) setPage(safePageIndex);

  const paginatedBooks = useMemo(
    () => books.slice(safePageIndex * PAGE_SIZE, (safePageIndex + 1) * PAGE_SIZE),
    [books, safePageIndex],
  );

  return (
    <div className="gallery">
      <BookListComponent books={paginatedBooks} loading={loading} />

      {!loading && books.length > PAGE_SIZE && (
        <div className="pagination">
          <button
            className="btn btn-secondary"
            disabled={safePageIndex === 0}
            onClick={() => setPage((p) => p - 1)}
          >
            &laquo; Prev
          </button>
          <span className="page-info">
            Page {safePageIndex + 1} of {totalPages}
          </span>
          <button
            className="btn btn-secondary"
            disabled={safePageIndex >= totalPages - 1}
            onClick={() => setPage((p) => p + 1)}
          >
            Next &raquo;
          </button>
        </div>
      )}
    </div>
  );
}
