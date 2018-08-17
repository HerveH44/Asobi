import React from "react";
import {string, number} from "prop-types";

import {Spaced} from "../../utils";

const Header = ({siteTitle, users, players, games, error}) => (
    <header>
        <h1 className="lobby-header">
            {siteTitle}
        </h1>
        <ServerInfo
                    numUsers={users}
                    numPlayers={players}
                    numActiveGames={games}/>
        <ApplicationError err={error}/>
    </header>
);

Header.propTypes = {
    siteTitle: string.isRequired,
    users: number.isRequired,
    players: number.isRequired,
    games: number.isRequired,
    error: string
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
