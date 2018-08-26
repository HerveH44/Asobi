import React from "react";
import {string, func} from "prop-types";
import {connect} from "react-redux";
import {getDeckInTxt} from "../../../../reducers/playerState";
import {clickCopyToClipboard} from "../../../../actions/game";
import {hashDeck} from "../../../../actions/server";

const Copy = ({deck, onClickCopy, hashDeck}) => (
    <div className='copy-controls connected-container'>
        <button
            className='connected-component'
            onClick={() => { hashDeck(); onClickCopy(deck); }}>
            Copy deck to clipboard
        </button>
    </div>
);

Copy.propTypes = {
    deck: string.isRequired,
    onClickCopy: func.isRequired,
    hashDeck: func.isRequired
};

const mapStateToProps = ({playerState}) => ({
    deck: getDeckInTxt(playerState)
});

const mapDispatchToProps = {
    hashDeck, onClickCopy: clickCopyToClipboard
};

export default connect(mapStateToProps, mapDispatchToProps)(Copy);
