function DataTable({
    page,
    columns = [],
    actions,
    sortField,
    sortDirection,
    onSort
}){
    function getNestedValue(obj, path){
        return path.split(".").reduce((acc, part) => acc?.[part], obj);
    }
    return (
        <div className="table-wrapper mt-0">
            <div className="table-responsive">
                <table className="table table-striped table-hover align-middle data-table">
                    <thead>
                        <tr>
                            {
                                columns.map(column => (
                                    <th key={column.key} 
                                        style={{cursor:"pointer"}}
                                        onClick={() => onSort(column.key)}>
                                        {column.label}
                                        {
                                            sortField === column.key && (
                                                <span className="ms-1">
                                                    {
                                                        sortDirection === "asc"?"↑":"↓"
                                                    }
                                                </span>
                                            )
                                        }
                                    </th>
                                ))
                            }
                            {
                                actions && (
                                    <th className="col-acoes">Ações</th>
                                )
                            }
                        </tr>
                    </thead>
                    <tbody>
                        {
                            page?.content?.length === 0 && (
                                <tr>
                                    <td colSpan={columns.length + 1} className="text-center">
                                        Nenhum registro encontrado.
                                    </td>
                                </tr>
                            )
                        }
                        {
                            page?.content?.map(item => (
                                <tr key={item?.id || ""}>
                                    {
                                        columns.map(column => (
                                            <td key={column.key}>
                                                {
                                                    (() => {
                                                        const value = getNestedValue(item, column.key);
                                                        return column.render
                                                        ?
                                                        column.render(
                                                            item[column.key],
                                                            item
                                                        )
                                                        : value;
                                                    })()
                                                }
                                            </td>
                                        ))
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