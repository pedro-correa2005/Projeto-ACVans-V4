function Pagination({

    page,

    onPageChange
}) {

    if (!page) {
        return null;
    }

    return (

        <div className="d-flex justify-content-center mt-4">

            <nav>

                <ul className="pagination">

                    <li
                        className={
                            `
                            page-item
                            ${page.first
                                ? "disabled"
                                : ""}
                            `
                        }
                    >

                        <button

                            className="page-link"

                            onClick={() =>
                                onPageChange(
                                    page.number - 1
                                )
                            }
                        >

                            Anterior

                        </button>

                    </li>

                    {
                        [...Array(page.totalPages)]
                            .map((_, index) => (

                                <li

                                    key={index}

                                    className={
                                        `
                                    page-item
                                    ${page.number === index
                                            ? "active"
                                            : ""
                                        }
                                    `
                                    }
                                >

                                    <button

                                        className="page-link"

                                        onClick={() =>
                                            onPageChange(index)
                                        }
                                    >

                                        {index + 1}

                                    </button>

                                </li>
                            ))
                    }

                    <li

                        className={
                            `
                            page-item
                            ${page.last
                                ? "disabled"
                                : ""}
                            `
                        }
                    >

                        <button

                            className="page-link"

                            onClick={() =>
                                onPageChange(
                                    page.number + 1
                                )
                            }
                        >

                            Próxima

                        </button>

                    </li>

                </ul>

            </nav>

        </div>
    );
}

export default Pagination;