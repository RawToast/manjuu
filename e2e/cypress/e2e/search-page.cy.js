describe('search page', () => {
  beforeEach(() => {
    cy.visit('/')

    cy.title().should('include', '基礎')
  })

  it('can search for authorities', () => {
    cy.get('[data-testid="searchbar-input"]').should('exist').type('Amber')
    cy.get('[data-testid="search-authorities-group"]').should('exist')
  })

  it('can show no result', () => {
    cy.get('[data-testid="searchbar-input"]').should('be.visible').type('Bananarama')

    cy.get('[data-testid="search-authorities-group"]').should('not.be.visible')
  })

  it('can navigate to an authority', () => {
    cy.get('[data-testid="searchbar-input"]').should('exist').type('Amber')
    cy.get('[data-testid="search-authorities-group"]').should('exist')

    cy.get('[data-testid="result-amber-valley"]').should('be.visible').first().click()

    cy.url().should('include', 'authority')

    cy.get('h3').contains('Amber Valley')
  })
})
