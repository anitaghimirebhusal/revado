import { useState } from 'react';

const EMPTY = { title: '', author: '', genre: '', pages: '', publishedYear: '' };

export default function BookSubmissionComponent({ onSubmit, onCancel }) {
  const [form, setForm] = useState(EMPTY);
  const [errors, setErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: null }));
  }

  function validate() {
    const errs = {};
    if (!form.title.trim()) errs.title = 'Title is required';
    if (!form.author.trim()) errs.author = 'Author is required';
    if (!form.genre.trim()) errs.genre = 'Genre is required';
    if (!form.pages || Number(form.pages) < 1) errs.pages = 'Pages must be at least 1';
    if (!form.publishedYear || Number(form.publishedYear) < 1) errs.publishedYear = 'Enter a valid year';
    return errs;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const errs = validate();
    if (Object.keys(errs).length > 0) {
      setErrors(errs);
      return;
    }
    setSubmitting(true);
    try {
      await onSubmit({
        title: form.title.trim(),
        author: form.author.trim(),
        genre: form.genre.trim(),
        pages: Number(form.pages),
        publishedYear: Number(form.publishedYear),
      });
      setForm(EMPTY);
    } catch {
      setErrors({ _global: 'Failed to create book. Please try again.' });
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="submission-panel">
      <h2>Add a New Book</h2>
      <form className="book-form" onSubmit={handleSubmit}>
        {errors._global && <div className="form-error-global">{errors._global}</div>}

        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input id="title" name="title" value={form.title} onChange={handleChange} placeholder="Book title" />
          {errors.title && <span className="form-error">{errors.title}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="author">Author</label>
          <input id="author" name="author" value={form.author} onChange={handleChange} placeholder="Author name" />
          {errors.author && <span className="form-error">{errors.author}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="genre">Genre</label>
          <input id="genre" name="genre" value={form.genre} onChange={handleChange} placeholder="e.g. Fiction, Science" />
          {errors.genre && <span className="form-error">{errors.genre}</span>}
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="pages">Pages</label>
            <input id="pages" name="pages" type="number" min="1" value={form.pages} onChange={handleChange} placeholder="Pages" />
            {errors.pages && <span className="form-error">{errors.pages}</span>}
          </div>
          <div className="form-group">
            <label htmlFor="publishedYear">Year</label>
            <input id="publishedYear" name="publishedYear" type="number" min="1" value={form.publishedYear} onChange={handleChange} placeholder="e.g. 2024" />
            {errors.publishedYear && <span className="form-error">{errors.publishedYear}</span>}
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-primary" disabled={submitting}>
            {submitting ? 'Creating...' : 'Create Book'}
          </button>
          <button type="button" className="btn btn-ghost" onClick={onCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
}
