type TodoPaginationProps = {
  page: number;
  totalPages: number;
  totalElements: number;
  isFirst: boolean;
  isLast: boolean;
  onPageChange: (page: number) => void;
};

export function TodoPagination({
  page,
  totalPages,
  totalElements,
  isFirst,
  isLast,
  onPageChange,
}: TodoPaginationProps) {
  if (totalPages <= 1) {
    return null;
  }

  return (
    <nav className="pagination" aria-label="Todo pagination">
      <p>
        Page {page + 1} of {totalPages} ({totalElements} tasks)
      </p>
      <div>
        <button
          type="button"
          className="button button-secondary"
          disabled={isFirst}
          onClick={() => onPageChange(page - 1)}
        >
          Previous
        </button>
        <button
          type="button"
          className="button button-secondary"
          disabled={isLast}
          onClick={() => onPageChange(page + 1)}
        >
          Next
        </button>
      </div>
    </nav>
  );
}
