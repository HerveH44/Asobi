import React from "react";
import {bool, string, array, func} from "prop-types";

import {onChangeLand} from "../../actions/game";
import {connect} from "react-redux";
import LandsPanel from "./LandsPanel";
import DownloadPanel from "./DownloadPanel";

const DeckSettings = ({didGameStart}) => (
    didGameStart
        ? <div className='deck-settings'>
            <LandsPanel/>
            {/*<DownloadPanel/>*/}
        </div>
        : <div/>
);

DeckSettings.propTypes = {
    didGameStart: bool.isRequired,
};

const mapStateToProps = ({gameState}) => ({
    didGameStart: gameState.didGameStart
});
const mapDispatchToProps = {
    onChangeLand
};

export default connect(mapStateToProps, mapDispatchToProps)(DeckSettings);
