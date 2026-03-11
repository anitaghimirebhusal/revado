export default function SidebarComponent({ genres, authors, activeGenre, activeAuthor, onGenreSelect, onAuthorSelect }) {
  return (
    <aside className="sidebar">
      <div className="sidebar-section">
        <h3>Genre</h3>
        <ul className="filter-list">
          {genres.map((genre) => (
            <li key={genre}>
              <button
                className={`filter-btn ${activeGenre === genre ? 'active' : ''}`}
                onClick={() => onGenreSelect(genre)}
              >
                {genre}
              </button>
            </li>
          ))}
        </ul>
      </div>

      <div className="sidebar-section">
        <h3>Author</h3>
        <ul className="filter-list">
          {authors.map((author) => (
            <li key={author}>
              <button
                className={`filter-btn ${activeAuthor === author ? 'active' : ''}`}
                onClick={() => onAuthorSelect(author)}
              >
                {author}
              </button>
            </li>
          ))}
        </ul>
      </div>
    </aside>
  );
}
