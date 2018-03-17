import React from "react";
import { connect } from 'react-redux';
import { string, number } from "prop-types";

import {Spaced} from "../utils";

const Header = ({ siteTitle, ...props }) => (
    <header>
        <h1 className="lobby-header">
            {siteTitle}
        </h1>
        <ServerInfo {...props}/>
        <ApplicationError/>
    </header>
);

Header.propTypes = {
  siteTitle: string.isRequired
};

const ApplicationError = ({ err }) => (
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

const mapStateToProps = (state) => ({
  ...state
});

export default connect(
  mapStateToProps,
  null
)(Header);
