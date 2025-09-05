import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import LoginForm from '@/components/LoginForm.vue'

const RouterLinkStub = {
  name: 'RouterLink',
  props: ['to'],
  template: `<a data-testid="router-link" :href="typeof to==='string' ? to : (to?.path || '/')"><slot/></a>`
}

describe('LoginForm.vue', () => {
  it('renderiza encabezado, campos requeridos, botón y enlace de registro', () => {
    const wrapper = mount(LoginForm, {
      global: { stubs: { RouterLink: RouterLinkStub } }
    })

    expect(wrapper.get('h2').text()).toBe('Iniciar Sesión')
    expect(wrapper.get('input[type="email"]').attributes('required')).toBeDefined()
    expect(wrapper.get('input[type="password"]').attributes('required')).toBeDefined()

    expect(wrapper.find('button[type="submit"]').exists()).toBe(true)

    const link = wrapper.get('[data-testid="router-link"]')
    expect(link.text()).toContain('Regístrate aquí')
    expect(link.attributes('href')).toBe('/signup')
  })

  it('al enviar el formulario muestra el mensaje de login', async () => {
    const wrapper = mount(LoginForm, {
      global: { stubs: { RouterLink: RouterLinkStub } }
    })

    await wrapper.get('input[type="email"]').setValue('andy@example.com')
    await wrapper.get('input[type="password"]').setValue('Secreta123')
    await wrapper.get('form').trigger('submit.prevent')

    const msg = wrapper.get('p.error')
    expect(msg.text()).toBe('Login enviado (Falta conectar con backend)')
  })
})
