import { useState, useEffect, useCallback } from 'react';
import { getAllBooks, createBook } from './api/bookService';
import SidebarComponent from './components/SidebarComponent';
import BookGalleryComponent from './components/BookGalleryComponent';
import BookSubmissionComponent from './components/BookSubmissionComponent';
import './App.css';

function App() {
  const [books, setBooks] = useState([]);
  const [allBooks, setAllBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeGenre, setActiveGenre] = useState(null);
  const [activeAuthor, setActiveAuthor] = useState(null);
  const [showForm, setShowForm] = useState(false);

  const genres = [...new Set(allBooks.map((b) => b.genre))].sort();
  const authors = [...new Set(allBooks.map((b) => b.author))].sort();

  const fetchBooks = useCallback(async (genre, author) => {
    setLoading(true);
    try {
      const filters = {};
      if (genre) filters.genre = genre;
      if (author) filters.author = author;
      const data = await getAllBooks(filters);
      setBooks(data);
    } catch (err) {
      console.error('Failed to fetch books:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchAllBooks = useCallback(async () => {
    try {
      const data = await getAllBooks();
      setAllBooks(data);
    } catch (err) {
      console.error('Failed to fetch all books:', err);
    }
  }, []);

  useEffect(() => {
    fetchAllBooks();
    fetchBooks(null, null);
  }, [fetchAllBooks, fetchBooks]);

  function handleGenreSelect(genre) {
    const next = activeGenre === genre ? null : genre;
    setActiveGenre(next);
    fetchBooks(next, activeAuthor);
  }

  function handleAuthorSelect(author) {
    const next = activeAuthor === author ? null : author;
    setActiveAuthor(next);
    fetchBooks(activeGenre, next);
  }

  async function handleCreate(bookData) {
    await createBook(bookData);
    setShowForm(false);
    setActiveGenre(null);
    setActiveAuthor(null);
    const data = await getAllBooks();
    setAllBooks(data);
    setBooks(data);
  }

  return (
    <div className="app">
      <header className="app-header">
        <div className="header-content">
          <h1>BookShelf</h1>
          <button className="btn btn-primary" onClick={() => setShowForm((v) => !v)}>
            {showForm ? 'Back to Gallery' : '+ Add Book'}
          </button>
        </div>
      </header>

      {showForm ? (
        <main className="app-main">
          <BookSubmissionComponent onSubmit={handleCreate} onCancel={() => setShowForm(false)} />
        </main>
      ) : (
        <div className="app-layout">
          <SidebarComponent
            genres={genres}
            authors={authors}
            activeGenre={activeGenre}
            activeAuthor={activeAuthor}
            onGenreSelect={handleGenreSelect}
            onAuthorSelect={handleAuthorSelect}
          />
          <main className="app-main">
            {(activeGenre || activeAuthor) && (
              <div className="active-filters">
                {activeGenre && (
                  <span className="filter-tag">
                    Genre: {activeGenre}
                    <button onClick={() => handleGenreSelect(activeGenre)}>&times;</button>
                  </span>
                )}
                {activeAuthor && (
                  <span className="filter-tag">
                    Author: {activeAuthor}
                    <button onClick={() => handleAuthorSelect(activeAuthor)}>&times;</button>
                  </span>
                )}
              </div>
            )}
            <BookGalleryComponent books={books} loading={loading} />
          </main>
        </div>
      )}
    </div>
  );
}

export default App;
