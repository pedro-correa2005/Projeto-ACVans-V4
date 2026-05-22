import { useEffect, useState } from "react";

function AutoCompleteInput({
    value, onChange, onSelect, searchFunction, placeholder, displayField, disabled
}){
    const [options, setOptions] = useState([]);

    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if(!value || value.length < 2 || disabled){
            setOptions([]);
            return;
        }
        const timeout = setTimeout(async () => {
            try {
                setLoading(true);
                const data = await searchFunction(value, 0, "max");
                setOptions(data.content);
            } catch (error) {
                console.error(error);
            } finally {
                setLoading(false);
            }
        }, 300);
        return () => clearTimeout(timeout);
    }, [value, disabled, searchFunction]);
    return(
        <div className="position-relative">
            <input type="text" className="form-control" value={value !== null? value:""} placeholder={placeholder} onChange={(e) => onChange(e.target.value)} disabled={disabled}/>
            {
                loading && (
                    <div className="small mt-1">Buscando...</div>
                )
            }
            {
                options.length > 0 && (
                    <ul className="list-group position-absolute w-100 z-3">
                        {
                            options.map(option => (
                                <li key={option.id} className="list-group-item list-group-item-action" onClick={() => {onSelect(option); onChange(option[displayField]); setOptions([]);}}> 
                                    {
                                        option[displayField]
                                    }
                                </li>
                            ))
                        }
                    </ul>
                )
            }
        </div>
    );
}

export default AutoCompleteInput;