function DataTable({
    data = [],
    columns = [],
    actions
}){
    return (
        <div className="table-responsive">
            <table className="table table-striped table-hover align-middle data-table">
                <thead>
                    <tr>
                        {
                            columns.map(column => (
                                <th key={column.key}>
                                    {column.lable}
                                </th>
                            ))
                        }
                        {
                            actions && (
                                <th>Ações</th>
                            )
                        }
                    </tr>
                </thead>
                <tbody>
                    {
                        data.length === 0 && (
                            <tr>
                                <td colSpan={columns.length + 1} className="text-center">
                                    Nenhum registro encontrado.
                                </td>
                            </tr>
                        )
                    }
                    {
                        data.map(item => (
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
                                        <td>
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
    );
}

export default DataTable;