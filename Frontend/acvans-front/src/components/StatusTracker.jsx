function StatusTracker({
    etapas,
    etapaAtualId
}) {
    function etapaConcluida(etapa){
        const atual = etapas.find(
            e => e.id === etapaAtualId
        );

        if(!atual){
            return false;
        }

        return etapa.ordem <= atual.ordem;
    }

    return (
        <section className="status-tracker">
            {etapas.map((etapa, index) => (

            <div
            key={etapa.id}
            className={
                etapaConcluida(etapa)
                ? "step completed"
                : "step"
            }
            >

                <div className="step-circle">
                    {index + 1}
                </div>

                <div>

                    <div className="step-title">
                    {etapa.titulo}
                    </div>

                    <div className="step-desc">
                    {etapa.descricao}
                    </div>

                </div>

            </div>

            ))}

        </section>
    );
}

export default StatusTracker;