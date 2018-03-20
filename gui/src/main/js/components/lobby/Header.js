import React from "react";
import { connect } from 'react-redux';
import { string, number } from "prop-types";
import AppBar from "material-ui/AppBar";
import Toolbar from "material-ui/Toolbar";
import Typography from 'material-ui/Typography';

import {Spaced} from "../utils";

const Header = ({ siteTitle, numUsers, numPlayers, numActiveGames, err }) => (
  <AppBar position="static" color="default">
    <Toolbar>
      <Typography variant="title" color="inherit">
        {siteTitle}
        <ServerInfo numUsers={numUsers} numPlayers={numPlayers} numActiveGames={numActiveGames}/>
        <ApplicationError err={err}/>
      </Typography>
    </Toolbar>
  </AppBar>
);

Header.propTypes = {
  siteTitle: string.isRequired,
  numUsers: number,
  numPlayers: number,
  numActiveGames: number,
  err: string
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

const mapStateToProps = ({def}) => ({
  ...def
});

export default connect(
  mapStateToProps,
  null
)(Header);
