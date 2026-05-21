import {

    useSortable

} from "@dnd-kit/sortable";

import {

    CSS

} from "@dnd-kit/utilities";

import {

    FaEdit,
    FaTrash,
    FaGripVertical

} from "react-icons/fa";

function SortableEtapaItem({

    etapa,

    editarEtapa,

    deletarEtapa

}){

    const {

        attributes,

        listeners,

        setNodeRef,

        transform,

        transition

    } = useSortable({

        id: etapa.id
    });

    const style = {

        transform:
          CSS.Transform.toString(
            transform
          ),

        transition
    };

    return (

        <li

            ref={setNodeRef}

            style={style}

            className="
                list-group-item
                d-flex
                justify-content-between
                align-items-center
            "
        >

            <div className="d-flex align-items-center gap-2">

                <span

                    {...attributes}

                    {...listeners}

                    style={{
                        cursor: "grab"
                    }}
                >
                    <FaGripVertical/>
                </span>

                <div>

                    <strong>
                        {etapa.titulo}
                    </strong>

                    : {etapa.descricao}

                </div>

            </div>

            <div className="btn-group btn-group-sm">

                <button
                    type="button"
                    className="
                        btn btn-outline-secondary
                    "
                    onClick={() =>
                        editarEtapa(etapa)
                    }
                >
                    <FaEdit/>
                </button>

                <button
                    type="button"
                    className="
                        btn btn-outline-danger
                    "
                    onClick={() =>
                        deletarEtapa(etapa)
                    }
                >
                    <FaTrash/>
                </button>

            </div>

        </li>
    );
}

export default SortableEtapaItem;