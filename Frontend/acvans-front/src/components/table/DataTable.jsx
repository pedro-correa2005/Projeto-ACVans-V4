function DataTable({
    page,
    columns = [],
    actions
}){
    return (
        <div className="table-wrapper">
            <div className="table-responsive">
                <table className="table table-striped table-hover align-middle data-table">
                    <thead>
                        <tr>
                            {
                                columns.map(column => (
                                    <th key={column.key}>
                                        {column.label}
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
                                <tr key={item.id}>
                                    {
                                        columns.map(column => (
                                            <td key={column.key}>
                                                {
                                                    column.render
                                                    ?
                                                    column.render(
                                                        item[column.key],
                                                        item
                                                    )
                                                    :item[column.key]
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