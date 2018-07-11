import React, { Component } from "react"
import { string, func, object } from "prop-types"
import uniqueId from "lodash.uniqueid"

class Card extends Component {
    constructor(props) {
        super(props);
        this.state = {
            url: `http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=${props.card.multiverseid}&type=card`
        };
        //   this.onMouseEnter = this.onMouseEnter.bind(this);
        //   this.onMouseLeave = this.onMouseLeave.bind(this);
    }

    // onMouseEnter() {
    //   if(this.props.card.isDoubleFaced) {
    //     this.setState({
    //       url: this.props.card.flippedCardURL
    //     });
    //   }
    // }

    // onMouseLeave() {
    //   if(this.props.card.isDoubleFaced) {
    //     this.setState({
    //       url: this.props.card.url
    //     });
    //   }
    // }

    render() {
        const { card, zoneName, pick } = this.props;
        const isAutopickable = zoneName === "pack" && card.isAutopick;

        const className =
            `card ${isAutopickable ? "autopick-card " : ""}
        card ${card.foil ? "foil-card " : ""}`;

        const title
            = isAutopickable
                ? "This card will be automatically picked if your time expires."
                : "";
        return (
            <span key={uniqueId()}
                className={className}
                title={title}
                onClick={() => pick(card.id)}
            // onMouseEnter={this.onMouseEnter}
            // onMouseLeave={this.onMouseLeave}
            >
                <img src={this.state.url} alt={card.name} />
            </span>
        );
    }
}

Card.propTypes = {
    card: object.isRequired,
    zoneName: string.isRequired,
    pick: func.isRequired
}

export default Card;

