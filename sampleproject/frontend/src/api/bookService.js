import axios from 'axios';

const API = axios.create({ baseURL: '/books' });

export async function getAllBooks(filters = {}) {
  const params = {};
  if (filters.author) params.author = filters.author;
  if (filters.genre) params.genre = filters.genre;
  const { data } = await API.get('', { params });
  return data;
}

export async function getBookById(id) {
  const { data } = await API.get(`/${id}`);
  return data;
}

export async function createBook(book) {
  const { data } = await API.post('', book);
  return data;
}
