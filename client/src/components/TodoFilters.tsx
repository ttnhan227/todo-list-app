type TodoFiltersProps = {
  search: string;
  completedFilter: boolean | null;
  onSearchChange: (search: string) => void;
  onCompletedFilterChange: (completed: boolean | null) => void;
};

export function TodoFilters({
  search,
  completedFilter,
  onSearchChange,
  onCompletedFilterChange,
}: TodoFiltersProps) {
  return (
    <section className="filters" aria-label="Todo filters">
      <label className="field search-field">
        <span>Search</span>
        <input
          value={search}
          onChange={(event) => onSearchChange(event.target.value)}
          placeholder="Search title or description"
        />
      </label>

      <div className="status-filter" role="group" aria-label="Filter by status">
        <button
          type="button"
          className={completedFilter === null ? "filter-button active" : "filter-button"}
          onClick={() => onCompletedFilterChange(null)}
        >
          All
        </button>
        <button
          type="button"
          className={completedFilter === false ? "filter-button active" : "filter-button"}
          onClick={() => onCompletedFilterChange(false)}
        >
          Active
        </button>
        <button
          type="button"
          className={completedFilter === true ? "filter-button active" : "filter-button"}
          onClick={() => onCompletedFilterChange(true)}
        >
          Completed
        </button>
      </div>
    </section>
  );
}
