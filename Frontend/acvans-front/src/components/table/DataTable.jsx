function DataTable({
    page,
    columns = [],
    actions,
    sortField,
    sortDirection,
    onSort
}) {

    function getNestedValue(obj, path) {
        return path
            .split(".")
            .reduce((acc, part) => acc?.[part], obj);
    }

    // Se vier Page => usa content
    // Se vier array => usa diretamente
    const data = Array.isArray(page)
        ? page
        : page?.content || [];

    return (
        <div className="table-wrapper mt-0">
            <div className="table-responsive">

                <table className="table table-striped table-hover align-middle data-table">

                    <thead>
                        <tr>

                            {
                                columns.map(column => (
                                    <th
                                        key={column.key}
                                        style={{
                                            cursor: onSort ? "pointer" : "default"
                                        }}
                                        onClick={() => onSort?.(column.key)}
                                    >

                                        {column.label}

                                        {
                                            sortField === column.key && (
                                                <span className="ms-1">
                                                    {
                                                        sortDirection === "asc"
                                                            ? "↑"
                                                            : "↓"
                                                    }
                                                </span>
                                            )
                                        }

                                    </th>
                                ))
                            }

                            {
                                actions && (
                                    <th className="col-acoes">
                                        Ações
                                    </th>
                                )
                            }

                        </tr>
                    </thead>

                    <tbody>

                        {
                            data.length === 0 && (
                                <tr>
                                    <td
                                        colSpan={columns.length + (actions ? 1 : 0)}
                                        className="text-center"
                                    >
                                        Nenhum registro encontrado.
                                    </td>
                                </tr>
                            )
                        }

                        {
                            data.map(item => (
                                <tr key={item?.id || Math.random()}>

                                    {
                                        columns.map(column => {

                                            const value = getNestedValue(
                                                item,
                                                column.key
                                            );

                                            return (
                                                <td key={column.key}>

                                                    {
                                                        column.render
                                                            ? column.render(value, item)
                                                            : value
                                                    }

                                                </td>
                                            );
                                        })
                                    }

                                    {
                                        actions && (
                                            <td className="col-acoes">
                                                {
                                                    actions(item)
                                                }
                                            </td>
                                        )
                                    }

                                </tr>
                            ))
                        }

                    </tbody>

                </table>

            </div>
        </div>
    );
}

export default DataTable;