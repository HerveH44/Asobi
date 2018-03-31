import {WEBSOCKET_MESSAGE} from "redux-middleware-websocket";

const websocket = ({dispatch}) => next => action => {
    const {type, payload: {data}} = action;
    if(!type || type !== WEBSOCKET_MESSAGE) {
        return next(action);
    }
    dispatch(JSON.parse(data))
}

export default websocket;
