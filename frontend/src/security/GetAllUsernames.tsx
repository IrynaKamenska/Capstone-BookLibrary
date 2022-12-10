import React, {useEffect, useState} from 'react';
import axios from "axios";


function GetAllUsernames() {
    const [names, setNames] = useState<string[]>([]);
    const getUsernames = () => {
        axios.get("/api/app-users/getAllUsernames")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(data => setNames(data))
    }
    useEffect(() => {
        getUsernames()
    }, [])

    function getSelect() {
        return <select onChange={getUsernames}>
            {names.map(name => {
                return (
                    <option value={name}>{name}</option>
                )
            })}
        </select>;
    }

    return (
        <section>
            <h1>Names</h1>
            {getSelect()}
        </section>
    );
}

export default GetAllUsernames;