export default function BookListComponent({ books, loading }) {
  if (loading) {
    return <div className="status-message">Loading books...</div>;
  }

  if (books.length === 0) {
    return (
      <div className="status-message empty">
        No books found matching these filters.
      </div>
    );
  }

  return (
    <div className="book-grid">
      {books.map((book) => (
        <div key={book.id} className="book-card">
          <div className="book-card-top">
            <h3 className="book-title">{book.title}</h3>
            <span className="genre-badge">{book.genre}</span>
          </div>
          <p className="book-author">by {book.author}</p>
          <div className="book-meta">
            <span>{book.pages} pages</span>
            <span>{book.publishedYear}</span>
          </div>
        </div>
      ))}
    </div>
  );
}
