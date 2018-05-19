import React from "react";
import {string, number} from "prop-types";

import {Spaced} from "../utils";

const Header = ({siteTitle, numUsers, numPlayers, numActiveGames, err}) => (
    <header>
        <h1 className="lobby-header">
            {siteTitle}
        </h1>
        <ServerInfo
                    numUsers={numUsers}
                    numPlayers={numPlayers}
                    numActiveGames={numActiveGames}/>
        <ApplicationError err={err}/>
    </header>
);

Header.propTypes = {
    siteTitle: string.isRequired,
    numUsers: number,
    numPlayers: number,
    numActiveGames: number,
    err: string
};

const ApplicationError = ({err}) => (
    <p className='error'>{err}</p>
);

ApplicationError.propTypes = {
    err: string
};

const ServerInfo = ({numUsers, numPlayers, numActiveGames}) => {
    const users = `${numUsers} ${numUsers === 1
        ? "user"
        : "users"} connected`;

    const players = `${numPlayers}
     ${numPlayers === 1
        ? "player"
        : "players"}
     playing ${numActiveGames}
       ${numActiveGames === 1
            ? "game"
            : "games"}`;

    return <p><Spaced elements={[users, players]}/></p>;
};

ServerInfo.propTypes = {
    numUsers: number,
    numPlayers: number,
    numActiveGames: number
};
export default Header;
