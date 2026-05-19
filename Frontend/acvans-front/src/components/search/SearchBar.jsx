function SearchBar({
    value,
    onChange,
    placeholder = "Pesquisar..."
}){
    return(
        <div className="input-group">
            <input type="text" id="searchInput" name="searchInput" className="form-control" placeholder={placeholder} value={value} onChange={(e) => onChange(e.target.value)}/>
        </div>
    );
}

export default SearchBar;