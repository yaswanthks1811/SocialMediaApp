import { useState } from "react";
import { viewMessages } from "../services/api";

function MessageButton(toUserId){
    const [toUserId,setToUserId] =useState('');

    const handleMessage = async()=>{
        <Link to={`/viewMessages?senderId=${toUserId}`}>
                
              </Link>
    }
    <div>
        <button onSubmit={handleMessage}>Message</button>
    </div>

}